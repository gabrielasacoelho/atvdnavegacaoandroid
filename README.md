# Diário de Treinos

**Tema escolhido:** Diário de Treinos

## Telas implementadas
- **Splash** → navega sozinha; quem decide o destino (Login ou Main) é o `NavHost`, via `vm.logado`.
- **Login** → botão "Entrar".
- **Painel Principal** → Bottom Navigation com 3 abas: Treinos (lista), Estatísticas e Perfil.
- **Lista** → botões Adicionar/Limpar, mensagem de estado vazio, clique abre o Detalhe.
- **Detalhe** → recebe `Route.Detail(id)` e lê com `toRoute()`.

## Requisitos atendidos
- Rotas com `sealed interface` + `@Serializable` (`Routes.kt`).
- Bottom Navigation com `saveState`, `restoreState` e `launchSingleTop`.
- `AppViewModel` único, criado na `MainActivity` e passado por parâmetro.
- Paleta própria com `lightColorScheme(...)` em `Theme.kt`.
- Todo `Icon` tem `contentDescription`.

## Como rodar no Android Studio
1. Crie um projeto "Empty Activity" (Compose) com pacote `com.example.diario`.
2. Substitua/adicione os arquivos `.kt` deste repositório no pacote.
3. Adicione o plugin `org.jetbrains.kotlin.plugin.serialization` (mesma versão do Kotlin) e as dependências:
   ```kotlin
   implementation("androidx.navigation:navigation-compose:2.8.5")
   implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
   implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
   implementation("androidx.activity:activity-compose:1.9.3")
   ```
4. Sincronize o Gradle e rode (▶).
