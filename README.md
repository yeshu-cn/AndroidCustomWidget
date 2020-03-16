# AndroidCustomWidget
 android custom view demo

## colorsbar
    
   * 使用canvas.clipPaht()裁减出来一个圆角矩形的画布，然后在画布上不同的区间颜色
   
   ```
   canvas.save()
   mPath.addRoundRect(
                mRectF, radius, radius, Path.Direction.CW
            )
   canvas.clipPath(mPath)
   canvas.restore()
   ```
   
   * addRoundRect两个参数半径，分别代表x轴和y轴上的半径。圆角是一个类似椭圆的东西。
		
		https://juejin.im/entry/58c233330ce463005460be3b
   
   * 文字垂直居中时注意计算文字的y坐标
   
   		公式：`rect.centerY - top/2 - bottom/2`
   		https://blog.csdn.net/zly921112/article/details/50401976
	

   
   