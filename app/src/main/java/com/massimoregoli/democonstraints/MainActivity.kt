package com.massimoregoli.democonstraints

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import coil.compose.AsyncImage
import com.massimoregoli.democonstraints.ui.theme.*
import com.massimoregoli.democonstraints.viewmodel.MyState.*
import com.massimoregoli.democonstraints.viewmodel.MyViewModel
import com.massimoregoli.democonstraints.viewmodel.ProductViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoConstraintsTheme {
                val context = LocalContext.current
                var refresh by rememberSaveable {
                    mutableStateOf(Load)
                }
                var message by rememberSaveable {
                    mutableStateOf("")
                }

                val vm: MyViewModel =
                    viewModel(
                        factory =
                        ProductViewModelFactory(context.applicationContext as Application)
                    )
                vm.getData {
                    refresh = Error
                    message = it
                }
                vm.productList.observe(this) {
                    refresh = Success
                }


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (refresh) {
                        Success -> {
                            ProductList(vm)
                        }
                        Error -> {
                            ErrorMessage(message)
                        }
                        Load, Init -> {
                            Loading()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorMessage(message: String) {
    ConstraintLayout {
        val msg = createRef()

        Text(
            text = message, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
                .constrainAs(msg) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            fontSize = 16.sp
        )
    }
}

@Composable
fun Loading() {
    ConstraintLayout {
        val (pi, wp) = createRefs()
        CircularProgressIndicator(modifier = Modifier.constrainAs(pi) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        })
        Text(text = "Wait, please", modifier = Modifier
            .constrainAs(wp) {
                top.linkTo(pi.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductList(
    vm: MyViewModel
) {
    LazyColumn {

        itemsIndexed(vm.productList.value!!) { index, it ->

            ListItem(text = {
                Column {
                    if (index == 0 || vm.productList.value!![index - 1].category != it.category) {
                        Text(
                            text = it.category.capitalize(Locale.current),
                            textAlign = TextAlign.Center,
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 24.sp
                        )
                    }
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = Color(0xFFFCFCFC),
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    ) {
                        ConstraintLayout {
                            val (title, desc, price,
                                rating, brand, thumb) = createRefs()

                            Text(
                                text = "${it.title} (${it.category})",
                                modifier = Modifier
                                    .constrainAs(title) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                fontSize = 18.sp,
                                color = Color.Blue
                            )
                            Text(
                                text = it.description,
                                modifier = Modifier
                                    .constrainAs(desc) {
                                        top.linkTo(brand.bottom, 8.dp)
                                        start.linkTo(title.start)
                                        end.linkTo(title.end)
                                    }
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFF0F0F0))
                                    .padding(8.dp),
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
                                text = "Price: ${it.price} $",
                                modifier = Modifier
                                    .constrainAs(price) {
                                        top.linkTo(desc.bottom, 2.dp)
                                        start.linkTo(desc.start)

                                    },
                                textAlign = TextAlign.Start,
                                fontSize = 14.sp,
                                color = Color.Black
                            )

                            Row(
                                modifier = Modifier
                                    .constrainAs(rating) {
                                        top.linkTo(price.top)
                                        end.linkTo(desc.end)
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
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                            AsyncImage(it.thumbnail, null,
                                modifier = Modifier
                                    .constrainAs(thumb)
                                    {
                                        top.linkTo(rating.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom, 8.dp)
                                    }
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                    .clip(RoundedCornerShape(4.dp))
                                    .padding(4.dp))
                        }
                    }

                }
            })
        }

    }
}


