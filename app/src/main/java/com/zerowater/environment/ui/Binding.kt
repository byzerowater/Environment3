package com.zerowater.environment.ui

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Environment
 * Class: Binding
 * Created by ZERO on 2020-05-19.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description:
 */


/**
 * 뷰 가시성 상태 설정
 *
 * @param view View
 * @param gone true 미노출, false 노출
 */
@BindingAdapter("visibleGone")
fun setVisibleGone(view: View, gone: Boolean) {
    view.visibility = if (gone) View.GONE else View.VISIBLE
}

/**
 * RecyclerViewBindingAdapters
 */
@InverseBindingMethods(
        InverseBindingMethod(type = RecyclerView::class, attribute = "android:recyclerViewAdapter"),
        InverseBindingMethod(type = RecyclerView::class, attribute = "android:supportsChangeAnimations"),
        InverseBindingMethod(type = RecyclerView::class, attribute = "android:itemAnimator"),
        InverseBindingMethod(type = RecyclerView::class, attribute = "android:itemDecoration"),
        InverseBindingMethod(type = RecyclerView::class, attribute = "android:items"))
class RecyclerViewBindingAdapters {
    companion object {
        /**
         * 어답터 설정
         *
         * @param recyclerView RecyclerView
         * @param adapter      설정할 어답터
         */
        @JvmStatic
        @BindingAdapter("android:recyclerViewAdapter")
        fun setRecyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
            adapter?.let {
                recyclerView.adapter = it
            }
        }

        /**
         * 아이템 데코레이션 설정
         *
         * @param recyclerView   RecyclerView
         * @param itemDecoration 설정할 데코레이션
         */
        @JvmStatic
        @BindingAdapter("android:itemDecoration")
        fun addItemDecoration(recyclerView: RecyclerView, itemDecoration: ItemDecoration?) {
            itemDecoration?.let {
                recyclerView.addItemDecoration(itemDecoration)
            }
        }

        /**
         * 애니메이션 지원 여부 설정
         *
         * @param recyclerView             RecyclerView
         * @param supportsChangeAnimations true 애니메이션 지원, false 애니메이션 미지원.
         */
        @JvmStatic
        @BindingAdapter("android:supportsChangeAnimations")
        fun setSupportsChangeAnimations(recyclerView: RecyclerView, supportsChangeAnimations: Boolean) {
            Timber.i("supportsChangeAnimations %s", supportsChangeAnimations)
            val animator = recyclerView.itemAnimator
            if (animator is SimpleItemAnimator) {
                Timber.i("SimpleItemAnimator %s", supportsChangeAnimations)
                animator.supportsChangeAnimations = supportsChangeAnimations
            }
        }

        /**
         * 애니메이션 설정
         *
         * @param recyclerView    RecyclerView
         * @param useItemAnimator true DefaultItemAnimator 설정, false 미설정
         */
        @JvmStatic
        @BindingAdapter("android:itemAnimator")
        fun setItemAnimator(recyclerView: RecyclerView, useItemAnimator: Boolean) {
            Timber.i("useItemAnimator %s", useItemAnimator)

            recyclerView.itemAnimator = if (useItemAnimator) DefaultItemAnimator() else null
        }

        /**
         * 아이템 설정
         *
         * @param recyclerView RecyclerView
         * @param adapter      RecyclerView.Adapter
         * @param dataList     설정할 아이템
         */
        @JvmStatic
        @BindingAdapter("android:recyclerViewAdapter", "android:items")
        fun bindItem(recyclerView: RecyclerView, adapter: ListAdapter<Nothing, Nothing>?, dataList: List<Nothing>?) {
            var varAdapter = adapter
            val oldAapater = recyclerView.adapter
            if (oldAapater != null) {
                varAdapter = oldAapater as ListAdapter<Nothing, Nothing>
            } else {
                recyclerView.adapter = varAdapter
            }

            dataList?.let {
                varAdapter?.submitList(dataList as List<Nothing>?)
            }
        }
    }
}

/**
 * NestedScrollViewViewBindingAdapters
 */
@InverseBindingMethods(
        InverseBindingMethod(type = NestedScrollView::class, attribute = "android:openKeyboard"))
class NestedScrollViewViewBindingAdapters {
    companion object {
        /**
         * 키보드 상태에 따라 스크롤 설정
         *
         * @param nestedScrollView NestedScrollView
         * @param isOpen           키보드 열린 상태
         */
        @JvmStatic
        @BindingAdapter("android:openKeyboard")
        fun setOpenKeyboard(nestedScrollView: NestedScrollView, isOpen: Boolean) {
            if (isOpen)
                nestedScrollView.post(Runnable {
                    nestedScrollView.scrollTo(0, nestedScrollView.bottom)
                })
        }
    }
}

/**
 * TextViewBindingAdapters
 */
@InverseBindingMethods(
        InverseBindingMethod(type = TextView::class, attribute = "android:htmlText"),
        InverseBindingMethod(type = TextView::class, attribute = "android:htmlTextHint"),
        InverseBindingMethod(type = TextView::class, attribute = "android:currentFormat"),
        InverseBindingMethod(type = TextView::class, attribute = "android:parseFormat"),
        InverseBindingMethod(type = TextView::class, attribute = "android:date"))
class TextViewBindingAdapters {
    companion object {
        /**
         * HTML형식 텍스트 변환
         *
         * @param view TextView
         * @param text          html 텍스트
         */
        @JvmStatic
        @BindingAdapter("android:htmlText")
        fun setHtmlText(view: TextView, text: String?) {
            text?.let {
                val htmlText = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) Html.fromHtml(text) else Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
                view.text = htmlText
            }
        }

        /**
         * HTML형식 텍스트 변환
         *
         * @param view TextView
         * @param text          html 텍스트
         */
        @JvmStatic
        @BindingAdapter("android:htmlTextHint")
        fun setHtmlTextHint(view: TextView, text: String?) {
            text?.let {
                val htmlText = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) Html.fromHtml(text) else Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
                view.hint = htmlText
            }
        }

        /**
         * HTML형식 텍스트 변환
         *
         * @param view TextView
         * @param text          html 텍스트
         */
        @JvmStatic
        @BindingAdapter("android:date", "android:currentFormat", "android:parseFormat")
        fun convertDateFormat(view: TextView, date: String?, currentFormat: String?, parseFormat: String?) {
            var parseDate: String? = date
            if (!date.isNullOrBlank()) {
                try {
                    val curSdf = SimpleDateFormat(currentFormat, Locale.getDefault())
                    val parSdf = SimpleDateFormat(parseFormat, Locale.getDefault())
                    parseDate = parSdf.format(curSdf.parse(date))
                } catch (e: ParseException) {
                    Timber.e(e)
                }
            }
            view.text = parseDate
        }
    }
}

/**
 * ImageViewBindingAdapters
 */
@InverseBindingMethods(
        InverseBindingMethod(type = ImageView::class, attribute = "android:iconImage"))
class ImageViewBindingAdapters {
    companion object {
        /**
         * 벡터 이미지 로드
         *
         * @param imageView ImageView
         * @param resource Int 이미지 이름
         */
        @JvmStatic
        @BindingAdapter("android:iconImage")
        fun setIconImage(imageView: ImageView, resource: Int) {
            imageView.setImageResource(resource)
        }
    }
}

