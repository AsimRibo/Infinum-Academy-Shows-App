package hr.asimr.shows_asim.models

import androidx.annotation.StringRes

data class FormDataStatus(var isValid: Boolean, @StringRes var messageId: Int?)
