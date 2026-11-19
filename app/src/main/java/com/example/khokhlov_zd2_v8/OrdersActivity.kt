package com.example.khokhlov_zd2_v8

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors

class OrdersActivity : AppCompatActivity() {

    private val dishes = listOf(
        Dish("Пицца Маргарита", 450),
        Dish("Бургер классический", 320),
        Dish("Суши-сет «Филадельфия»", 690),
        Dish("Паста Карбонара", 410),
        Dish("Салат Цезарь", 280),
        Dish("Картофель фри", 150),
        Dish("Лимонад", 120)
    )
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val dao = AppDatabase.get(this).orderDao()
        val spinner = findViewById<Spinner>(R.id.spDish)
        val etQty = findViewById<EditText>(R.id.etQty)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val rv = findViewById<RecyclerView>(R.id.rvOrders)

        spinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            dishes.map { "${it.name} — ${it.price} ₽" }
        )

        val adapter = OrderAdapter { item -> executor.execute { dao.delete(item) } }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        dao.getAll().observe(this) { list ->
            adapter.submitList(list)
            tvTotal.text = getString(R.string.total, list.sumOf { it.price * it.quantity })
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            val qty = etQty.text.toString().trim().toIntOrNull()
            if (qty == null || qty <= 0) {
                etQty.error = getString(R.string.error_qty)
                return@setOnClickListener
            }
            val dish = dishes[spinner.selectedItemPosition]
            executor.execute { dao.insert(OrderItem(dishName = dish.name, price = dish.price, quantity = qty)) }
            etQty.setText("1")
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            executor.execute { dao.clear() }
        }
    }
}

class OrderAdapter(private val onDelete: (OrderItem) -> Unit) :
    ListAdapter<OrderItem, OrderAdapter.VH>(object : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(a: OrderItem, b: OrderItem) = a.id == b.id
        override fun areContentsTheSame(a: OrderItem, b: OrderItem) = a == b
    }) {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.tvTitle)
        val sub: TextView = v.findViewById(R.id.tvSub)
        val delete: ImageButton = v.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.title.text = item.dishName
        holder.sub.text = "${item.quantity} × ${item.price} ₽ = ${item.quantity * item.price} ₽"
        holder.delete.setOnClickListener { onDelete(item) }
    }
}
