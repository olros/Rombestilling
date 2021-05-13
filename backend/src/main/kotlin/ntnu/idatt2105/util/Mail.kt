package ntnu.idatt2105.util

import com.sun.istack.NotNull

class Mail(
    private val from: String,
    private val to: String,
    private val subject: String,
    private val htmlTemplate: HtmlTemplate
)
