package com.storge

import com.domain.ViewModel

/**
 * This class, and the module as well, is a temporary solution till storage layer is implemented,
 * its dependency with domain should go out
 */
object DataSource {

    fun getItems(): MutableList<ViewModel> {
        val items: MutableList<ViewModel> = mutableListOf()

        (1..100).forEachIndexed { index, i ->
            val item = ViewModel(index, "Element" + index)
            items.add(item)
        }

        return items
    }
}
