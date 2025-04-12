package com.github.karolhor.ala.discounts.repository.db.codec

import com.github.karolhor.ala.discounts.repository.db.model.DiscountPolicy
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.convert.EnumWriteSupport


@WritingConverter
class DiscountPolicyWriterConverter : EnumWriteSupport<DiscountPolicy>()
