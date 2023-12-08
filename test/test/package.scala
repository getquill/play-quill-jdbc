import java.io.File
import play.api.{Application, ApplicationLoader, Environment, Mode}

package object test {
  def fakeApp: Application = {
    val appLoader = new AppLoader

    val context = ApplicationLoader.createContext(
      new Environment(new File("."), ApplicationLoader.getClass.getClassLoader, Mode.Test)
    )
    appLoader.load(context)
  }
}
