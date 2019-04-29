# libj
Misc useful Java functions .

## Usage

For example, in Ubuntu,

```
mkdir /javalibs

cd /javalibs

git clone https://github.com/topxeq/libj

vi ~/.profile
```

(In MacOS, use ~/.bash_profile instead)

Add one line to set Java CLASSPATH as below:

```
export CLASSPATH=.:/javalibs/libj:/javalibs/libj/json-20180813.jar:/javalibs/libj/dom4j-2.1.1.jar:/javalibs/libj/jaxen-1.1.6.jar:/javalibs/libj/ojdbc8.jar
```

then,

```
mkdir /javaprjs

cd /javaprjs

mkdir Test

cd Test

touch Test.java

vi Test.java
```

Enter following code:

```
import org.topget.TXGT.*;

public class Test {

        public static void main(String[] args) {

                TXGT.Pl("This is a test.");

        }

}
```

then,

```
javac Test.java
java Test
```
