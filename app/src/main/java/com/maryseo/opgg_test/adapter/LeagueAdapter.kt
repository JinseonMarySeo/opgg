//package com.maryseo.opgg_test.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.ListAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.maryseo.opgg_test.data.League
//import java.text.DecimalFormat
//
//class LeagueAdapter: ListAdapter<League, LeagueAdapter.ItemHolder>(ItemDiffCallback()) {
//    private lateinit var context: Context
//    private val decFormat = DecimalFormat("#,###")
//
//    inner class ItemHolder(private val binding: ItemLeagueInfoBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(position: Int) {
//            val league = currentList[position]
//            Glide.with(context)
//                .load(league.tierRank.imageUrl)
//                .into(binding.ivChallenger)
//            binding.tvLeagueName.text = league.tierRank.name
//            binding.tvLeagueTier.text = league.tierRank.tier
//            binding.tvLeagueLp.text = String.format(context.getString(R.string.format_league_lp), decFormat.format(league.tierRank.lp))
//            val percent = (league.wins.toFloat() / (league.wins + league.losses)) * 100f
//            binding.tvLeagueRecord.text = String.format(context.getString(R.string.format_record_percent), league.wins, league.losses, percent.toInt())
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
//        context = parent.context
//        return ItemHolder(ItemLeagueInfoBinding.inflate(LayoutInflater.from(context), parent, false))
//    }
//
//    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
//        holder.bind(position)
//    }
//
//    override fun submitList(list: List<League>?) {
//        val currentList = currentList.toMutableList()
//        list?.let { currentList.addAll(list) }
//        super.submitList(currentList)
//    }
//}
//
//class ItemDiffCallback : DiffUtil.ItemCallback<League>() {
//    override fun areItemsTheSame(oldItem: League, newItem: League): Boolean {
//        return oldItem.tierRank.name == newItem.tierRank.name
//    }
//
//    override fun areContentsTheSame(oldItem: League, newItem: League): Boolean {
//        return oldItem == newItem
//    }
//}