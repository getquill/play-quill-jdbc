import java.io.File

import play.api.{Environment, ApplicationLoader, Mode}

package object test {
  def fakeApp = {
    val appLoader = new AppLoader

    val context = ApplicationLoader.createContext(
      new Environment(new File("."), ApplicationLoader.getClass.getClassLoader, Mode.Test)
    )
    appLoader.load(context)
  }
}
