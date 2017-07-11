import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
@SupportedSourceVersion(SourceVersion.RELEASE_0)
public interface PersonDao {
    Person getPerson();
}