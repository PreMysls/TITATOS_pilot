package com.example.tictactoeonsteroids.data

import com.example.tictactoeonsteroids.enums.AttributeEnum
import com.example.tictactoeonsteroids.enums.AttributeValueEnum

class GameButton(var background: AttributeValueEnum = AttributeValueEnum.DEFAULT,
                 var color: AttributeValueEnum = AttributeValueEnum.DEFAULT,
                 var scale: AttributeValueEnum = AttributeValueEnum.DEFAULT) {

    fun getAttribute(attributeEnum: AttributeEnum) = when(attributeEnum){
        AttributeEnum.BACKGROUND -> background
        AttributeEnum.COLOR -> color
        AttributeEnum.SCALE -> scale
        AttributeEnum.NONE -> AttributeValueEnum.DEFAULT
    }
}
