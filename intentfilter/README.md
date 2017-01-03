##Action

要指定接受的**Intent**操作，**Intent**过滤器既可以不声明任何 <action> 元素，也可以声明多个此类元素。例如：

```xml
<intent-filter>
    <action android:name="android.intent.action.EDIT" />
    <action android:name="android.intent.action.VIEW" />
    ...
</intent-filter>
```

要通过此过滤器，您在**Intent**中指定的action必须与过滤器中列出的某一操作匹配。

如果该过滤器未列出任何操作，则**Intent**没有任何匹配项，因此所有**Intent**均无法通过测试。 但是，如果**Intent**未指定操作，则会通过测试（只要过滤器至少包含一个操作）。


##Category

要指定接受的**Intent**类别，**Intent**过滤器既可以不声明任何 <category> 元素，也可以声明多个此类元素。 例如：

```xml
<intent-filter>
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    ...
</intent-filter>
```

若要使**Intent**通过类别测试，则**Intent**中的每个类别均必须与过滤器中的类别匹配。反之则未必然, **Intent**过滤器声明的类别可以超出**Intent**中指定的数量，且**Intent**仍会通过测试。 因此，不含类别的**Intent**应当始终会通过此测试，无论过滤器中声明何种类别均是如此。

>注：Android 会自动将 CATEGORY_DEFAULT 类别应用于传递给 startActivity() 和 startActivityForResult() 的所有隐式 Intent。因此，如需 Activity 接收隐式 Intent，则必须将 "android.intent.category.DEFAULT" 的类别包括在其注：Android 会自动将 CATEGORY_DEFAULT 类别应用于传递给 startActivity() 和 startActivityForResult() 的所有隐式 Intent。因此，如需 Activity 接收隐式 Intent，则必须将 "android.intent.category.DEFAULT" 的类别包括在其**Intent**过滤器中（如上文的 <intent-filter> 示例所示）。**Intent**过滤器中（如上文的 <intent-filter> 示例所示）。