/*
Copyright © 2019 Salt Edge. https://saltedge.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.saltedge.sdk.sample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saltedge.sdk.model.SETransaction;
import com.saltedge.sdk.sample.R;
import com.saltedge.sdk.sample.utils.DateTools;

import java.util.ArrayList;

public class TransactionsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<SETransaction> transactionsList;

    public TransactionsAdapter(Context context, ArrayList<SETransaction> transactionsList) {
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.transactionsList = transactionsList;
    }

    @Override
    public int getCount() {
        return transactionsList.size();
    }

    @Override
    public SETransaction getItem(int position) {
        return transactionsList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.list_item_transaction, parent, false);
        TextView title = rowView.findViewById(R.id.title);
        TextView subtitleLeft = rowView.findViewById(R.id.subtitleLeft);
        TextView subtitleRight = rowView.findViewById(R.id.subtitleRight);
        SETransaction transaction = getItem(position);
        title.setText(transaction.getDescription());
        subtitleLeft.setText(DateTools.formatDateToString(transaction.getMadeOnDate()));
        String subTitle = transaction.getCurrencyCode() + " " + transaction.getAmount();
        subtitleRight.setText(subTitle);
        return rowView;
    }
}
