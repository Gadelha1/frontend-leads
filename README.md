# Gerenciador de Leads Imobiliários

Link do Repositório: <https://github.com/RudeBoyOne/frontend-leads>

Aplicação cliente multiplataforma (Android, Desktop e Web) do sistema de gestão imobiliária. Ela
consome a API do backend para centralizar os serviços internos, como: receber leads dos portais de
imóveis, distribuí-los para os corretores responsáveis, acompanhar o processo de vendas e cadastrar
e gerenciar imóveis. A interface é escrita uma única vez em Compose Multiplatform e compartilhada
entre as três plataformas.

## Tecnologias utilizadas

- Kotlin 2.4.10
- Kotlin Multiplatform (Android, Desktop JVM e Web/Wasm)
- Compose Multiplatform 1.11.1
- Compose Material 3 1.11.0-alpha07
- Android Gradle Plugin 9.1.0 (compileSdk 36 / minSdk 24 / targetSdk 37)
- Ktor Client 3.5.1 (OkHttp no Android, CIO no Desktop, JS no Web)
- Koin 4.2.2 (injeção de dependências)
- Kotlinx Serialization 1.11.0
- Kotlinx Coroutines 1.11.0
- AndroidX Lifecycle 2.11.0 (ViewModel + Compose)
- AndroidX Navigation 2.9.2
- Gradle Wrapper 9.7.1

---

### Integrantes e entidades

| Integrante | Entidade 1 | Entidade 2 |
| --- | --- | --- |
| [Allan Chagas] | [PropertySensor] | [SensorMetric] |
| [Gabriel Alves] | [Property] | [Lead] |
| [Lucas Ferreira] | [RealStateAgency] | [BranchOffice] |
| [Rafael Mateus] | [Team] | [Activity] |
| [Ygor Lopes] | [Role] | [User] |

---

## Rodando o projeto

São necessários o **JDK 17+** e o **Gradle Wrapper** (não é preciso instalar o Gradle: o
`./gradlew` resolve a versão sozinho). Para o alvo Android é necessário o **Android SDK** e, para a
Web, apenas um navegador.

O frontend consome o backend em `http://localhost:8080` (no emulador Android, usa
`http://10.0.2.2:8080`). Suba o backend antes de abrir a aplicação.

Desktop:

```bash
./gradlew :desktopApp:run
Web (dev server com hot reload na porta 3500):
./gradlew :webApp:wasmJsBrowserDevelopmentRun
Android (APK de debug):
./gradlew :androidApp:assembleDebug
Testes do código compartilhado:
./gradlew :shared:desktopTest
```
## Organização do código
O código compartilhado vive em shared/src/commonMain/kotlin, sob o pacote
br.com.semesperanca.frontendleads, em camadas:
```
data/          DTOs, cliente HTTP (Ktor), repositórios e armazenamento local
domain/        contratos de repositório
di/            injeção de dependências (Koin)
presentation/  telas e ViewModels (MVI), componentes de UI e navegação
theme/         tema do Compose
Cada plataforma tem um módulo fino que apenas inicializa o Koin e chama o App() compartilhado:
shared/       todo o código e a UI compartilhada (commonMain + androidMain + desktopMain + wasmJsMain)
androidApp/   entrypoint Android (MainActivity)
desktopApp/   entrypoint Desktop (janela Compose)
webApp/       entrypoint Web (Wasm + host HTML)
```
