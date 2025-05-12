package com.droidcon.habitsync.viewmodel

sealed class AddEditMode {
    object Add : AddEditMode()
    data class Edit(val habitId: String) : AddEditMode()
}
