package com.example.auctioninginfoapp.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

/** 화면(Fragment)에 표시된 소프트 키보드를 숨김으로 전환하는 확장 함수 (hideKeyboard()) 선언
 * - 소프트 키보드를 제어할 때 InputMethodManager를 사용
 * . 키보드를 보여줄 때 : inputMethodManager.showSoftInput()
 * . 키보드를 감출 때 : inputMethodManager.hideSoftInputFromWindow()
 */

fun Fragment.hideKeyboard() {
    /** 현재 화면(UI)의 소프트 키보드를 가져온다 */
    val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    /** 현재 화면(UI)을 가져와서 소프트 키보드를 숨김 */
    requireActivity().currentFocus?.let {focusView ->
        /** 화면에 출력되어있는 소프트 키보드를 숨김으로 전환 */
        inputMethodManager.hideSoftInputFromWindow(
            focusView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )

    }
}