package net.kaczmarzyk.spring.data.jpa.web;

import net.kaczmarzyk.spring.data.jpa.utils.TypeUtil;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Disjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.NativeWebRequest;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SpecificationUtils {

    private static SimpleSpecificationResolver simpleResolver = new SimpleSpecificationResolver();
    private static OrSpecificationResolver orResolver = new OrSpecificationResolver();
    private static DisjunctionSpecificationResolver disjunctionResolver = new DisjunctionSpecificationResolver();
    private static ConjunctionSpecificationResolver conjunctionResolver = new ConjunctionSpecificationResolver();
    private static AndSpecificationResolver andResolver = new AndSpecificationResolver();

    private static List<Class<? extends Annotation>> annotationTypes = Arrays.asList(Spec.class, Or.class, And.class, Conjunction.class, Disjunction.class);

    /**
     * Resolves the given parameters and interface to an actual {@link Specification}.
     * @param params a {@link MultiValueMap} of parameters
     * @param specClass the {@link Class} of the annotated spec interface
     * @param <T> the interface which extends {@link Specification}
     * @return an instance of a {@link Specification} from the desired class
     */
    public static <T extends Specification<?>> T resolveSpec(final MultiValueMap<String, String> params, final Class<T> specClass) {


        final NativeWebRequest webRequest = Mockito.mock(NativeWebRequest.class);
        Mockito.when(webRequest.getParameterValues(Mockito.anyString())).thenAnswer(args -> {
            final List<String> result = params.getOrDefault(args.getArgument(0), Collections.emptyList());
            return result.toArray(new String[result.size()]);
        });


        final List<Specification<?>> ifaceSpecs = resolveSpecFromInterfaceAnnotations(webRequest, specClass);

        if (ifaceSpecs.isEmpty()) {
            return null;
        }

        final Specification<Object> spec = new net.kaczmarzyk.spring.data.jpa.domain.Conjunction(ifaceSpecs);

        return EnhancerUtil.wrapWithIfaceImplementation(specClass, spec);
    }

    private static List<Specification<?>> resolveSpecFromInterfaceAnnotations(final NativeWebRequest webRequest, final Class<?> specClass) {
        final List<Specification<?>> result = new ArrayList<>();

        final Collection<Class<?>> ifaceTree = TypeUtil.interfaceTree(specClass);

        for (final Class<?> iface : ifaceTree) {
            if (!isAnnotated(iface)) {
                continue;
            }
            final Object specDef = getAnnotation(iface);
            result.add(buildSpecification(webRequest, specDef));
        }

        return result;
    }

    private static Specification<Object> buildSpecification(final NativeWebRequest webRequest, final Object specDef) {
        final Specification<Object> spec;

        if (specDef instanceof Spec) {
            spec = simpleResolver.buildSpecification(webRequest, (Spec) specDef);
        } else if (specDef instanceof Or) {
            spec = orResolver.buildSpecification(webRequest, (Or) specDef);
        } else if (specDef instanceof Disjunction) {
            spec = disjunctionResolver.buildSpecification(webRequest, (Disjunction) specDef);
        } else if (specDef instanceof Conjunction) {
            spec = conjunctionResolver.buildSpecification(webRequest, (Conjunction) specDef);
        } else if (specDef instanceof And) {
            spec = andResolver.buildSpecification(webRequest, (And) specDef);
        } else {
            throw new IllegalStateException();
        }
        return spec;
    }

    private static boolean isAnnotated(final Class<?> target) {
        return getAnnotation(target) != null;
    }

    private static Object getAnnotation(final Class<?> target) {
        for (final Class<? extends Annotation> annotationType : annotationTypes) {
            final Annotation potentialAnnotation = target.getAnnotation(annotationType);
            if (potentialAnnotation != null) {
                return potentialAnnotation;
            }
        }
        return null;
    }
}
