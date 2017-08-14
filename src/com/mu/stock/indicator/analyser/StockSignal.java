package com.mu.stock.indicator.analyser;

/**
 *
 * @author Peng Mu
 */
public enum StockSignal
{
    macd_cross_x_up,
    macd_cross_x_down,
    macd_signal_cross_x_up,
    macd_signal_cross_x_down,
    macd_cross_signal_up,
    macd_cross_signal_down,

    rsi_over_bought_70,
    rsi_over_bought_80,
    rsi_over_sold_30,
    rsi_over_sold_20,

    k_hammer,
    k_hanging_man,
    k_inverted_hammer,
    k_shooting_star,
    k_bull_engulf,
    k_bear_engulf,
    k_bull_harami,
    k_bear_harami,
    k_doji,
    k_evening_star,
    k_morning_star,
    k_downside_tasuki_gap,
    k_upside_tasuki_gap,
    k_moring_star_doji,
    k_evening_star_doji,
    k_stick_sandwich,
    k_dark_cloud,
    k_piercing,
    k_three_black_crows,
    k_three_white_soldiers,

    mfi_over_bought_70,
    mfi_over_bought_80,
    mfi_over_sold_30,
    mfi_over_sold_20,

    williams_over_bought,
    williams_over_bought_more,
    williams_over_sold,
    williams_over_sold_more,

    stochastic_over_bought,
    stochastic_over_sold,

    roc_over_bought,
    roc_over_sold,


    bottom_10_days,
    peak_10_days,
    bottom_20_days,
    peak_20_days,
    bottom_60_days,
    peak_60_days,

    break_10_day_resistance,
    break_20_day_resistance,
    break_30_day_resistance,
    break_60_day_resistance,
    break_90_day_resistance,
    break_180_day_resistance,

    break_10_day_support,
    break_20_day_support,
    break_30_day_support,
    break_60_day_support,
    break_90_day_support,
    break_180_day_support,

    sma_10_cross_200_up,
    sma_10_cross_200_down,
    sma_upper_zone_10_200,
    sma_lower_zone_10_200,
    sma_mid_zone_10_200,
    sma_enter_lower_zone,
    sma_enter_upper_zone,
}
