package com.baboci.exchangerate

object TestData {

    fun convertTest(): String = """
			{
			    "amount": 10,
			    "currency": "EUR/TRY"
			}
		""".trimIndent()

    fun xmlReferenceRates() = """
        <gesmes:subject>Reference rates</gesmes:subject>
            <gesmes:Sender>
                <gesmes:name>European Central Bank</gesmes:name>
            </gesmes:Sender>
            <Cube>
                <Cube time="2022-01-21">
                    <Cube currency="USD" rate="1.1348"/>
                    <Cube currency="JPY" rate="129.14"/>
                </Cube>
            </Cube>
    """.trimIndent()
}