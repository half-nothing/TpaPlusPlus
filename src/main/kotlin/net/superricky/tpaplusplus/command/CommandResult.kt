package net.superricky.tpaplusplus.command

enum class CommandResult(val status: Int) {
    NORMAL(1),
    SENDER_NOT_EXIST(-1),
    RECEIVER_NOT_EXIST(-2),
    SELF_CHECK_ERROR(-3)
}
