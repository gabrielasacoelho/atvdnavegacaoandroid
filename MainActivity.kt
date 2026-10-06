package com.example.diario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    // ViewModel único, criado uma vez aqui
    private val vm: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DiarioTheme { AppNav(vm) } }
    }
}

// ---------- NavHost principal: Splash -> Login/Main -> Detail ----------
@Composable
fun AppNav(vm: AppViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Route.Splash) {

        composable<Route.Splash> {
            SplashScreen(onFinished = {
                // A decisão é tomada AQUI (quem chama), não dentro da Splash
                val destino: Route = if (vm.logado) Route.Main else Route.Login
                nav.navigate(destino) { popUpTo(Route.Splash) { inclusive = true } }
            })
        }

        composable<Route.Login> {
            LoginScreen(onLogin = {
                vm.login()
                nav.navigate(Route.Main) { popUpTo(Route.Login) { inclusive = true } }
            })
        }

        composable<Route.Main> {
            MainScreen(vm, onOpenDetail = { id -> nav.navigate(Route.Detail(id)) })
        }

        composable<Route.Detail> { entry ->
            val rota = entry.toRoute<Route.Detail>()
            DetailScreen(vm, rota.id, onBack = { nav.popBackStack() })
        }
    }
}

// ---------- Telas ----------
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onFinished()
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Diário de Treinos", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onLogin) { Text("Entrar") }
    }
}

private data class TabItem(val route: Tab, val label: String, val icon: ImageVector)

private val tabs = listOf(
    TabItem(Tab.Treinos, "Treinos", Icons.AutoMirrored.Filled.List),
    TabItem(Tab.Stats, "Estatísticas", Icons.Filled.Star),
    TabItem(Tab.Perfil, "Perfil", Icons.Filled.Person)
)

// Painel principal com Bottom Navigation (NavController próprio das abas)
@Composable
fun MainScreen(vm: AppViewModel, onOpenDetail: (Int) -> Unit) {
    val tabNav = rememberNavController()
    val backStack by tabNav.currentBackStackEntryAsState()
    val atual = backStack?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { item ->
                    NavigationBarItem(
                        selected = atual?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                        onClick = {
                            tabNav.navigate(item.route) {
                                popUpTo(tabNav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = "Aba ${item.label}") },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = tabNav,
            startDestination = Tab.Treinos,
            modifier = Modifier.padding(padding)
        ) {
            composable<Tab.Treinos> { ListaScreen(vm, onOpenDetail) }
            composable<Tab.Stats> { SimpleTab("Estatísticas") }
            composable<Tab.Perfil> { SimpleTab("Perfil") }
        }
    }
}

@Composable
fun SimpleTab(titulo: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(titulo, style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun ListaScreen(vm: AppViewModel, onOpenDetail: (Int) -> Unit) {
    val listState = rememberLazyListState() // preserva a rolagem
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.adicionar() }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar treino")
                Text("Adicionar")
            }
            OutlinedButton(onClick = { vm.limpar() }) {
                Icon(Icons.Filled.Delete, contentDescription = "Apagar todos os treinos")
                Text("Limpar")
            }
        }
        Spacer(Modifier.height(16.dp))

        if (vm.treinos.isEmpty()) {
            // Estado vazio tratado
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nenhum treino ainda. Toque em Adicionar!")
            }
        } else {
            LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.treinos, key = { it.id }) { treino ->
                    Card(
                        Modifier.fillMaxWidth().clickable { onOpenDetail(treino.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(treino.nome, Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(vm: AppViewModel, id: Int, onBack: () -> Unit) {
    val treino = vm.treinos.firstOrNull { it.id == id }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(24.dp)) {
            Text(treino?.nome ?: "Treino não encontrado", style = MaterialTheme.typography.headlineSmall)
            Text("ID: $id")
        }
    }
}
