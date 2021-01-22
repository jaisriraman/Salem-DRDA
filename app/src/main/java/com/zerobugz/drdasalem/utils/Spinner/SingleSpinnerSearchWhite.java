package com.zerobugz.drdasalem.utils.Spinner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import com.zerobugz.drdasalem.R;

import java.util.ArrayList;
import java.util.List;

public class SingleSpinnerSearchWhite extends AppCompatSpinner implements OnCancelListener {
    private static final String TAG = SingleSpinnerSearchWhite.class.getSimpleName();
    public static AlertDialog.Builder builder;
    public static AlertDialog ad;
    MyAdapter adapter;
    private List<KeyPairBoolData> items;
    private String defaultText = "";
    private String spinnerTitle = "";
    private SpinnerListener listener;

    public SingleSpinnerSearchWhite(Context context) {
        super(context);
    }

    public SingleSpinnerSearchWhite(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        TypedArray a = arg0.obtainStyledAttributes(arg1, R.styleable.MultiSpinnerSearch);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MultiSpinnerSearch_hintText) {
                spinnerTitle = a.getString(attr);
                defaultText = spinnerTitle;
                break;
            }
        }
        Log.i(TAG, "spinnerTitle: " + spinnerTitle);
        a.recycle();
    }

    public SingleSpinnerSearchWhite(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }


    public List<KeyPairBoolData> getSelectedItems() {
        List<KeyPairBoolData> selectedItems = new ArrayList<>();
        for (KeyPairBoolData item : items) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public List<Long> getSelectedIds() {
        List<Long> selectedItemsIds = new ArrayList<>();
        for (KeyPairBoolData item : items) {
            if (item.isSelected()) {
                selectedItemsIds.add(item.getId());
            }
        }
        return selectedItemsIds;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner

        String spinnerText = null;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                spinnerText = items.get(i).getName();
                break;
            }
        }
        if (spinnerText == null) {
            spinnerText = defaultText;
        }

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner_white, new String[]{spinnerText});
        setAdapter(adapterSpinner);

        if (adapter != null)
            adapter.notifyDataSetChanged();

        listener.onItemsSelected(items);
    }

    @Override
    public boolean performClick() {

        builder = new AlertDialog.Builder(getContext());
        builder.setTitle(spinnerTitle);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.alert_dialog_listview_search, null);
        builder.setView(view);

        final ListView listView = view.findViewById(R.id.alertSearchListView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setFastScrollEnabled(false);
        adapter = new MyAdapter(getContext(), items);

        listView.setAdapter(adapter);
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                listView.setSelection(i);
                break;
            }
        }
        final TextView emptyText = view.findViewById(R.id.empty);
        listView.setEmptyView(emptyText);

        EditText editText = view.findViewById(R.id.alertSearchEditText);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {

            Log.i(TAG, " ITEMS : " + items.size());
            dialog.cancel();
        });

        builder.setOnCancelListener(this);

        ad = builder.show();
        //ad.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return true;
    }

    public void setItems(List<KeyPairBoolData> items, int position, SpinnerListener listener) {

        this.items = items;
        this.listener = listener;

        for (KeyPairBoolData item : items) {
            if (item.isSelected()) {
                defaultText = item.getName();
                break;
            }
        }

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner_white, new String[]{defaultText});
        setAdapter(adapterSpinner);

        if (position != -1) {
            items.get(position).setSelected(true);
            //listener.onItemsSelected(items);
            onCancel(null);
        }
    }


    //Adapter Class
    public class MyAdapter extends BaseAdapter implements Filterable {

        List<KeyPairBoolData> arrayList;
        List<KeyPairBoolData> mOriginalValues; // Original Values
        LayoutInflater inflater;

        public MyAdapter(Context context, List<KeyPairBoolData> arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "getView() enter");
            ViewHolder holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.spinneritem_listview_single, parent, false);
            holder.textView = convertView.findViewById(R.id.alertTextView);
            convertView.setTag(holder);

            if (position % 2 == 0) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            } else {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            }

            final KeyPairBoolData data = arrayList.get(position);

            holder.textView.setText(data.getName());
            holder.textView.setTypeface(null, Typeface.NORMAL);

            convertView.setOnClickListener(v -> {
                for (int i = 0; i < items.size(); i++) {
                    items.get(i).setSelected(false);
                }
                int len = arrayList.size();
                for (int i = 0; i < len; i++) {
                    arrayList.get(i).setSelected(false);
                    if (i == position) {
                        arrayList.get(i).setSelected(true);
                        Log.i(TAG, "On Click Selected Item : " + arrayList.get(i).getName() + " : " + arrayList.get(i).isSelected());
                    }
                }
                ad.dismiss();
                SingleSpinnerSearchWhite.this.onCancel(ad);
            });

            if (data.isSelected()) {
                holder.textView.setTypeface(null, Typeface.BOLD);
                //   holder.textView.setTextColor(Color.BLACK);
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.QIBus_softui_gray));
            }
            return convertView;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public Filter getFilter() {
            return new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    arrayList = (List<KeyPairBoolData>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    List<KeyPairBoolData> FilteredArrList = new ArrayList<>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<>(arrayList); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            Log.i(TAG, "Filter : " + mOriginalValues.get(i).getName() + " -> " + mOriginalValues.get(i).isSelected());
                            String data = mOriginalValues.get(i).getName();
                            if (data.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(mOriginalValues.get(i));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
        }

        private class ViewHolder {
            TextView textView;
        }
    }
}