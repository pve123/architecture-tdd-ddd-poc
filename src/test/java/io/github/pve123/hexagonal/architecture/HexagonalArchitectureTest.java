package io.github.pve123.hexagonal.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * 헥사고날 아키텍처의 의존 방향을 테스트로 지킵니다. 누가 규칙을 어기면 빌드가 실패합니다.
 *
 * <pre>
 * adapter  →  application  →  domain
 *   (바깥)        (유스케이스)      (규칙)
 * </pre>
 */
@AnalyzeClasses(packages = "io.github.pve123.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule 도메인은_스프링과_JPA를_모른다 = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..", "jakarta.persistence..", "io.swagger..", "lombok..");

    @ArchTest
    static final ArchRule 도메인은_애플리케이션과_어댑터를_모른다 = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage("..application..", "..adapter..");

    @ArchTest
    static final ArchRule 애플리케이션은_어댑터를_모른다 = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..adapter..");

    @ArchTest
    static final ArchRule 게시판은_회원의_도메인과_저장소를_직접_쓰지_않는다 = noClasses()
            .that().resideInAPackage("..board..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "..member.domain..", "..member.application.port.out..", "..member.adapter.out..");
}
