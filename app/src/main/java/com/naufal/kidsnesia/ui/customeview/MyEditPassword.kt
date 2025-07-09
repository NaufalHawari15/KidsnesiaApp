package com.naufal.kidsnesia.ui.customeview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.naufal.kidsnesia.R

class MyEditPassword @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                when {
                    password.isEmpty() -> setError(context.getString(R.string.empy_pass), null)
                    password.contains(" ") -> setError(context.getString(R.string.no_spacy_pass), null)
                    password.length < 12 -> setError(context.getString(R.string.minimum_pass), null)
                    !password.contains(Regex("[A-Z]")) -> setError(context.getString(R.string.harus_ada_huruf_kapital), null)
                    !password.contains(Regex("[0-9]")) -> setError(context.getString(R.string.harus_ada_angka), null)
                    !password.contains(Regex("""[!@#\$%^&*()_+\-=\[\]{};':"\\|,.<>/?]""")) ->
                        setError(context.getString(R.string.harus_ada_karakter_spesial), null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
