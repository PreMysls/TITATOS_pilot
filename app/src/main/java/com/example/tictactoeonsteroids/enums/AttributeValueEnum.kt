package com.example.tictactoeonsteroids.enums

enum class AttributeValueEnum(val point: Int, val twoPoints: Int = point * 2, val threePoints: Int = point * 3) {
    DEFAULT(0),
    FIRST(1),
    SECOND(10);

    companion object{
        fun getOpposite(attributeValueEnum: AttributeValueEnum) = when(attributeValueEnum){
            DEFAULT -> DEFAULT
            FIRST -> SECOND
            SECOND -> FIRST
        }

        fun getValueByPoint(point: Int) = when(point) {
            FIRST.point -> FIRST
            SECOND.point -> SECOND
            else -> DEFAULT
        }
    }


}