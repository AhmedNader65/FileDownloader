package com.najwa.task.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.najwa.task.inflater

class BaseViewHolder<in T>(parent: ViewGroup, @LayoutRes layoutId: Int) :
    RecyclerView.ViewHolder(parent.inflater(layoutId)), LayoutContainer {

    override val containerView: View
        get() = itemView

}

public interface LayoutContainer {
    /** Returns the root holder view. */
    public val containerView: View?
}