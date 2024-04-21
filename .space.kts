job("Build and test") {
    container(image = "eclipse-temurin:21") {
        env["BETAGUESS_CLIENT_ID"] = "{{ project:betaguess_client }}"
        env["BETAGUESS_CLIENT_SECRET"] = "{{ project:betaguess_client_secret }}"
        kotlinScript { api ->
            api.gradlew("bootBuildImage")
        }
    }
}