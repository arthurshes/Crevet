package workwork.test.andropediagits.core.exception


import kotlin.Exception

class TooLongUserNameException:Exception("Too long username max 29 characters")

class TooBigPhotoException:Exception("Too big photo max size 128mb")

class NameIsEmptyException:Exception("Name is empty exception")

//class ThisNameAlreadyExistsException:Exception("This name already exists please create a new name")