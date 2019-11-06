package com.leige.tool

import java.awt.*
import javax.swing.JFrame

object MainFrame {

    /**
     * FlowLayout流式布局
    以行为单位，按照add()的顺序依次排列组件，一行排不下另起一行
     */
    var windowsWidth = 500
    var windowsHeight = 250

    private fun showFrame() {
        val frame = JFrame()

        frame.layout = FlowLayout() // 流氏布局

        frame.isVisible = true
        // 得到显示器屏幕的宽高
        val width = Toolkit.getDefaultToolkit().screenSize.width
        val height = Toolkit.getDefaultToolkit().screenSize.height
        frame.setBounds((width - windowsWidth) / 2,
                (height - windowsHeight) / 2, windowsWidth, windowsHeight)

        // 文字输入框
        val area = TextArea()
        area.setSize(420, 160)
        frame.add(area)

        val btn = Button("commit")
        btn.size = Dimension(80, 80)
        btn.background = Color.green
        frame.add(btn)

        val textField = TextField("", 50)
        frame.add(textField)

        btn.addActionListener {
            if (it.actionCommand == "commit") {
                val s = area.text
                if (s == null || s.isEmpty())
                    textField.text = "receive a null text.."
                else {
                    //val s = "compile group: 'org.jsoup', name: 'jsoup', version: '1.12.1'"
                    textField.text = ConvertJson.formatGradleDependencies(s).toString()
                }
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        showFrame()
    }
}
