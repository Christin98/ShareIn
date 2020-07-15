package com.project.christinkcdev.share.shareindia.app;

import com.project.christinkcdev.share.shareindia.widget.EditableListAdapter;

public interface EditableListFragmentModelImpl<V extends EditableListAdapter.EditableViewHolder> {
    void setLayoutClickListener(EditableListFragment.LayoutClickListener<V> clickListener);
}
