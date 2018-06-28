package com.anlijiu.example.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface AdapterDelegate<T> {

  /**
   * Called to determine whether this AdapterDelegate is the responsible for the given data
   * element.
   *
   * @param items The data source of the Adapter
   * @param position The position in the datasource
   * @return true, if this item is responsible,  otherwise false
   */
  public boolean isForViewType(@NonNull T items, int position);


}
