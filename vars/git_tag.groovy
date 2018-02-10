/** The Git Tag Message plugin doesn't work in pipelines so we have to add this **/
/** https://wiki.jenkins.io/display/JENKINS/Git+Tag+Message+Plugin **/

/** @return The tag name, or `null` if the current commit isn't a tag. */
String name() {
    commit = commit()
    if (commit) {
        desc = sh(script: "git describe --tags ${commit}", returnStdout: true)?.trim()
        if (is_tag(desc)) {
            return desc
        }
    }
    return ""
}

/** @return The tag message, or `null` if the current commit isn't a tag. */
String message() {
    name = name()
    msg = sh(script: "git tag -n10000 -l ${name}", returnStdout: true)?.trim()
    if (msg) {
        return msg.substring(name.size()+1, msg.size())
    }
    return null
}

String commit() {
    return sh(script: 'git rev-parse HEAD', returnStdout: true)?.trim()
}

@NonCPS
boolean is_tag(String desc) {
    match = desc =~ /.+-[0-9]+-g[0-9A-Fa-f]{6,}$/
    result = !match
    match = null // prevent serialisation
    return result
}
