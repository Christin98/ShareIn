package com.project.christinkcdev.share.sharein.app;

import com.project.christinkcdev.share.sharein.widget.EditableListAdapter;

public interface EditableListFragmentModelImpl<V extends EditableListAdapter.EditableViewHolder> {
    void setLayoutClickListener(EditableListFragment.LayoutClickListener<V> clickListener);
}
