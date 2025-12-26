def call(String status) {

    def job      = env.JOB_NAME
    def build    = env.BUILD_NUMBER
    def branch   = env.BRANCH_NAME ?: 'N/A'
    def url      = env.BUILD_URL
    def webhook  = env.DISCORD_WEBHOOK
    def message  = ""

    if (status == 'STARTED') {
        message = """
🚀 **Build Started**
• Job: **${job}**
• Build: **#${build}**
• Branch: **${branch}**
"""
    } 
    else if (status == 'SUCCESS') {
        message = """
✅ **Build Succeeded**
• Job: **${job}**
• Build: **#${build}**
• Branch: **${branch}**
🎉 All steps completed successfully!
"""
    } 
    else if (status == 'FAILURE') {
        message = """
❌ **Build Failed**
• Job: **${job}**
• Build: **#${build}**
• Branch: **${branch}**
⚠️ Please check logs for the failure reason.
"""
    }

    discordSend(
        webhookURL: webhook,
        description: message.trim(),
        link: url
    )
}

