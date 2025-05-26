package com.droidcon.habitsync.domain.model

/**
 * Represents the screen mode for the Add/Edit Habit UI.
 * It helps decide whether the screen should be in Add mode or Edit mode.
 */
sealed class AddEditMode {

    /**
     * Used when the user is adding a new habit.
     */
    object Add : AddEditMode()

    /**
     * Used when the user is editing an existing habit.
     * @property habitId The ID of the habit being edited.
     */
    data class Edit(val habitId: String) : AddEditMode()
}
