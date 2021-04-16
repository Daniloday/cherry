package com.jevely.cherry.di

import com.jevely.cherry.ui.game.GameViewModel
import com.jevely.cherry.ui.one.OneViewModel
import com.jevely.cherry.ui.result.ResultViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        GameViewModel(get())
    }
    viewModel {
        OneViewModel(get())
    }
    viewModel {
        ResultViewModel(get())
    }

}