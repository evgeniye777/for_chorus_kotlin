package com.example.for_chour_kotlin.presentations.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.for_chour_kotlin.data.source.url_responses.AuthorizationState
import com.example.for_chour_kotlin.data.typeData.appDataParticipant.AppDataParticipant

class DialogParticipantEdit(
    context: Context,
    private val participant: AppDataParticipant,
    private val position: Int,
    private val adapter: RecyclerView.Adapter<*>  // Адаптер для обновления UI
) : AlertDialog(context) {

    private lateinit var layout: LinearLayout
    private lateinit var progressBar: ProgressBar

    init {
        setTitle("Редактировать участника")

        // Основной layout
        layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
        }

        // ProgressBar для ожидания
        progressBar = ProgressBar(context).apply {
            visibility = View.GONE  // Скрыт по умолчанию
            // Для Material: используйте CircularProgressIndicator(context) если импортирован
        }
        layout.addView(progressBar)

        // Преобразования для предзаполнения
        val genderText = when (participant.pGender) {
            1 -> "Женский"
            2 -> "Мужской"
            else -> participant.pGender.toString()
        }
        val postText = participant.post.joinToString(", ")

        // Поля формы
        val etName = EditText(context).apply {
            hint = "Имя"
            setText(participant.pName ?: "")
        }
        val etGender = EditText(context).apply {
            hint = "Пол"
            setText(genderText)
        }
        val etAccess = EditText(context).apply {
            hint = "Доступ"
            setText(participant.access.toString())
        }
        val etPost = EditText(context).apply {
            hint = "Пост"
            setText(postText)
        }
        val etAllowed = EditText(context).apply {
            hint = "Разрешено"
            setText(participant.allowed.toString())
        }

        layout.addView(etName)
        layout.addView(etGender)
        layout.addView(etAccess)
        layout.addView(etPost)
        layout.addView(etAllowed)

        setView(layout)

        setButton(BUTTON_NEGATIVE, "Отмена") { dialog, _ ->
            dialog.dismiss()
        }
        setButton(BUTTON_POSITIVE, "Изменить") { dialog, _ ->
            // Валидация (пример)
            val accessInt = etAccess.text.toString().toIntOrNull()
            val allowedInt = etAllowed.text.toString().toIntOrNull()
            if (accessInt == null || allowedInt == null) {
                AuthorizationState.mainActivity?.vivod("Доступ и Разрешено должны быть числами")
                return@setButton
            }

            // Показываем ProgressBar, скрываем форму
            progressBar.visibility = View.VISIBLE
            for (i in 1 until layout.childCount) {  // Пропускаем ProgressBar (индекс 0)
                layout.getChildAt(i).visibility = View.GONE
            }
            // Отключаем кнопки (если нужно)
            getButton(BUTTON_POSITIVE).isEnabled = false
            getButton(BUTTON_NEGATIVE).isEnabled = false

            if (participant.hashName != null && AuthorizationState.groups?.focus?.value?.hashName != null) {
                AuthorizationState.typeResponses?.changePersonForGroup(
                    participant.hashName!!,
                    AuthorizationState.groups?.focus?.value?.hashName!!,
                    etName.text.toString(),
                    when (etGender.text.toString()) {
                        "Женский" -> 1
                        "Мужской" -> 2
                        else -> etGender.text.toString().toIntOrNull() ?: participant.pGender
                    }.toString(),
                    etAccess.text.toString(),
                    etPost.text.toString(),
                    etAllowed.text.toString(),
                    {
                        // Успех: обновляем данные
                        participant.pName = etName.text.toString()
                        participant.pGender = when (etGender.text.toString()) {
                            "Женский" -> 1
                            "Мужской" -> 2
                            else -> etGender.text.toString().toIntOrNull() ?: participant.pGender
                        }
                        participant.access = accessInt
                        participant.post = etPost.text.toString().split(", ").toMutableList()
                        participant.allowed = allowedInt

                        // Скрываем ProgressBar, показываем форму
                        progressBar.visibility = View.GONE
                        for (i in 1 until layout.childCount) {
                            layout.getChildAt(i).visibility = View.VISIBLE
                        }
                        getButton(BUTTON_POSITIVE).isEnabled = true
                        getButton(BUTTON_NEGATIVE).isEnabled = true

                        // Обновляем UI и закрываем
                        adapter.notifyItemChanged(position)
                        AuthorizationState.mainActivity?.vivod("Изменения сохранены")
                        dialog.dismiss()
                    }, {
                        // Ошибка: скрываем ProgressBar, показываем форму
                        progressBar.visibility = View.GONE
                        for (i in 1 until layout.childCount) {
                            layout.getChildAt(i).visibility = View.VISIBLE
                        }
                        getButton(BUTTON_POSITIVE).isEnabled = true
                        getButton(BUTTON_NEGATIVE).isEnabled = true

                        AuthorizationState.mainActivity?.vivod("Не удалось сохранить изменения (error server)")
                        dialog.dismiss()
                    }
                )
            } else {
                // Аналогично для null-данных
                progressBar.visibility = View.GONE
                for (i in 1 until layout.childCount) {
                    layout.getChildAt(i).visibility = View.VISIBLE
                }
                getButton(BUTTON_POSITIVE).isEnabled = true
                getButton(BUTTON_NEGATIVE).isEnabled = true

                AuthorizationState.mainActivity?.vivod("Не удалось сохранить изменения (data null)")
                dialog.dismiss()
            }
        }
    }
}
