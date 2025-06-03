package com.coded.loanlift.composables.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coded.loanlift.formStates.comments.CommentFormState

@Composable
fun CommentInputOverlay(
    isReply: Boolean = false,
    initialText: String = "",
    commentFormState: CommentFormState,
    updateCommentFormState: (CommentFormState) -> Unit,
    onCancel: () -> Unit,
    onSubmit: (String) -> Unit,
    isSubmitting: Boolean
) {
    var text by remember { mutableStateOf(initialText) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2A2B2E), shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            OutlinedTextField(
                value = if (isReply) text else commentFormState.message,
                onValueChange = {
                    if (isReply) text = it else updateCommentFormState(commentFormState.copy(message = it, messageError = null))
                },
                placeholder = {
                    Text(if (isReply) "Write a reply..." else "Write a comment...")
                },
                isError = commentFormState.messageError != null && !isReply,
                supportingText = {
                    if (!isReply) {
                        commentFormState.messageError?.let {
                            Text(it, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF2A2B2E),
                    unfocusedContainerColor = Color(0xFF2A2B2E)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (isReply) {
                            onSubmit(text)
                        } else {
                            val validated = commentFormState.validate()
                            if (!validated.isValid) {
                                updateCommentFormState(validated)
                                return@Button
                            }
                            onSubmit(validated.message.trim())
                        }
                    },
                    enabled = !isSubmitting && (if (isReply) text.isNotBlank() else commentFormState.message.isNotBlank()),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Submit")
                    }
                }
            }
        }
    }
}