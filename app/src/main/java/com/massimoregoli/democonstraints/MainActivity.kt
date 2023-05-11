package com.massimoregoli.democonstraints

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Star
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.massimoregoli.democonstraints.model.Product
import com.massimoregoli.democonstraints.ui.theme.DemoConstraintsTheme
import com.massimoregoli.democonstraints.viewmodel.MyViewModel
import com.massimoregoli.democonstraints.viewmodel.ToDoViewModelFactory

@OptIn(ExperimentalMaterialApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoConstraintsTheme {
                val context = LocalContext.current
                var products = mutableListOf<Product>()
                var refresh by rememberSaveable {
                    mutableStateOf(false)
                }

                val vm: MyViewModel =
                    viewModel(
                        factory =
                        ToDoViewModelFactory(context.applicationContext as Application)
                    )

                vm.productList.observe(this) {
                    products = it
                    products.sort()
                    refresh = !refresh
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    refresh = !refresh
                    LazyColumn {
                        itemsIndexed(products) { index, it ->
                            if (vm.productList.value?.get(index)?.isLoaded == false)
                                vm.getThumbnail(index)
                            ListItem(text = {
                                Column {
                                    if (index == 0 || products[index - 1].category != it.category) {
                                        Text(
                                            text = it.category.capitalize(Locale.current),
                                            textAlign = TextAlign.Center,
                                            color = Color.Red,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    Card(
                                        shape = RoundedCornerShape(8.dp),
                                        backgroundColor = Color(0xFFFAFAFA),
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth()
                                    ) {
                                        ConstraintLayout {
                                            val (title, desc, price,
                                                rating, brand, thumb) = createRefs()

                                            Text(
                                                text = "${it.title} (${it.category})",
                                                modifier = Modifier
                                                    .constrainAs(title) {
                                                        top.linkTo(parent.top, 4.dp)
                                                        start.linkTo(parent.start, 8.dp)
                                                        end.linkTo(parent.end, 8.dp)
                                                    }
                                                    .fillMaxWidth(),
                                                textAlign = TextAlign.Start,
                                                fontSize = 18.sp,
                                                color = Color.Blue
                                            )
                                            Text(
                                                text = it.description,
                                                modifier = Modifier
                                                    .constrainAs(desc) {
                                                        top.linkTo(brand.bottom, 4.dp)
                                                        start.linkTo(title.start)
                                                        end.linkTo(title.end)
                                                    }
                                                    .padding(4.dp)
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(Color.White)
                                                    .padding(2.dp),
                                                textAlign = TextAlign.Start,
                                                fontSize = 16.sp,
                                                color = Color.Black
                                                )

                                            Text(
                                                text = it.brand,
                                                modifier = Modifier
                                                    .constrainAs(brand) {
                                                        top.linkTo(title.bottom)
                                                        start.linkTo(title.start, 8.dp)
                                                    },
                                                textAlign = TextAlign.Start,
                                                fontSize = 16.sp,
                                                color = Color.Red
                                            )
                                            Text(
                                                text = "price: ${it.price} $",
                                                modifier = Modifier
                                                    .constrainAs(price) {
                                                        top.linkTo(desc.bottom, 4.dp)
                                                        start.linkTo(parent.start, 8.dp)
                                                    },
                                                textAlign = TextAlign.Start,
                                                fontSize = 16.sp,
                                                color = Color.Black
                                            )
                                            Row(
                                                modifier = Modifier
                                                    .constrainAs(rating) {
                                                        top.linkTo(price.top)
                                                        end.linkTo(parent.end, 8.dp)
                                                    },
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    Icons.TwoTone.Star, null,
                                                    tint = Color(0xFFFFD700)
                                                )
                                                Text(
                                                    text = "${it.rating}",

                                                    textAlign = TextAlign.Start,
                                                    fontSize = 16.sp,
                                                    color = Color.Black
                                                )
                                            }
                                            if (it.bitmap != null)
                                                Icon(it.bitmap!!, null,
                                                    tint = Color.Unspecified,
                                                    modifier = Modifier
                                                        .constrainAs(thumb)
                                                        {
                                                            top.linkTo(rating.bottom, 4.dp)
                                                            start.linkTo(parent.start, 4.dp)
                                                            end.linkTo(parent.end)
                                                            bottom.linkTo(parent.bottom, 8.dp)
                                                        }
                                                        .clip(RoundedCornerShape(4.dp))
                                                )
                                        }
                                    }

                                }
                            })
                        }

                    }
                }
            }
        }
    }
}

