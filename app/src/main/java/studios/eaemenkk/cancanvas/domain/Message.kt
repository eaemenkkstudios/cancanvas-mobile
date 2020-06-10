package studios.eaemenkk.cancanvas.domain

data class Message(val chatID: String,
                   val message: String,
                   val timestamp: String,
                   val sender: String)