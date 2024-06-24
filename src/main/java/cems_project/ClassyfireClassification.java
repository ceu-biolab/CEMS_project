package cems_project;

import java.util.Set;

public record ClassyfireClassification(String kingdom, String superClass, String ownClass, String subClass, String directParent, Set<String> alternativeParents) {
}
