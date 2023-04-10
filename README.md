# LoginApplication

A multimodule login app to show how to apply clean code, clean architecture, flows, coroutines, dependency injection (Hilt), Compose and testing (using Mockk and Espresso).
The app is composed by 3 modules:
1. Login module: Contains all the business logic to make the login flow, including use case, repository and fake API.
2. App XML: User interface made with XML (Android View System)
3. App Compose: User interface made with Compose.

IMPORTANT!: Because the app uses a fake API implementation, success or fail result is randomized.

You can run whatever App XML or App Compose as shown below:

<img width="349" alt="image" src="https://user-images.githubusercontent.com/5751275/230848650-759bdcf9-e791-42ee-a559-d9b36d25b5c6.png">

DEMO APP COMPOSE:

[app_login_demo_compose.webm](https://user-images.githubusercontent.com/5751275/230847573-22253d7e-b8f6-40b9-8f46-864d8605cd85.webm)

DEMO APP XML:

[app_login_demo_xml.webm](https://user-images.githubusercontent.com/5751275/230847721-fa61fb3e-f164-40fd-b115-8ea6c0a82902.webm)

Made with love using Kotlin.
