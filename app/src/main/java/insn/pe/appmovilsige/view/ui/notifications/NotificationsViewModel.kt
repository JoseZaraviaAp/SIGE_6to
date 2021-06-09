package insn.pe.appmovilsige.view.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Parece que no tienes emergencias aún, nos alegramos de ello"
    }
    val text: LiveData<String> = _text
}