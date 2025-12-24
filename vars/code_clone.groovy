def call(String repoUrl, String branch) {
    git url: "${repoUrl}", branch: "${branch}"
}
