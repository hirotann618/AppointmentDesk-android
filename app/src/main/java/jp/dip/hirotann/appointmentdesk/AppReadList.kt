package jp.dip.hirotann.appointmentdesk

import android.content.Context

class AppReadList private constructor(context: Context) {
    var readlist = mutableSetOf<String>()
    var eventname = ""

    companion object {
        private var _instance: AppReadList? = null

        fun onCreateApplication(applicationContext: Context) {
            // Application#onCreateのタイミングでシングルトンが生成される
            _instance = AppReadList(applicationContext)
        }

        val instance: AppReadList
            get() {
                _instance?.let {
                    return it
                } ?: run {
                    // nullにはならないはず
                    throw RuntimeException("AppState should be initialized!")
                }
            }
    }
}