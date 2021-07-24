package com.example.covid_19tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_list.view.*

class StateAdapter(val list: List<StatewiseItem>):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
      val view=convertView?: LayoutInflater.from(parent?.context).inflate(R.layout.item_list,parent,false)
      val item=list[position]
        view.ConfirmedTv.text=item.confirmed
        view.RecoveredTv.text=item.recovered
        view.DeathsTv.text=item.deaths
        view.ActiveTv.text=item.active
        view.stateTv.text=item.state
        return view
    }

    override fun getItem(position: Int): Any {
    return list[position]
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getCount(): Int {
      return list.size
    }

}