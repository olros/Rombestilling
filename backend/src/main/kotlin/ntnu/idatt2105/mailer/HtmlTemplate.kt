package ntnu.idatt2105.mailer

class HtmlTemplate(
    private val template: String,
    val props: Map<Int, String>
)
