job("Build and test") {
    container(image = "eclipse-temurin:21") {
        env["BETAGUESS_CLIENT_ID"] = "{{ project:BETAGUESS_CLIENT_ID }}"
        env["BETAGUESS_CLIENT_SECRET"] = "{{ project:BETAGUESS_CLIENT_SECRET }}"
        kotlinScript { api ->
            api.gradlew("bootBuildImage")
        }
    }
}