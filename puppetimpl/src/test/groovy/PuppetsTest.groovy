import tv.puppetmaster.data.i.InstallablePuppet
import tv.puppetmaster.data.i.ParentPuppet
import tv.puppetmaster.data.i.Puppet
import tv.puppetmaster.data.i.SourcesPuppet

import static groovy.io.FileType.FILES

def puppetClassNames = []
def puppetFullyQualifiedClassNames = []
new File('./puppetimpl/src/main/groovy/').eachFileRecurse(FILES) {
    if (it.name.endsWith('Puppet.groovy')) {
        puppetClassNames << it.name.replaceFirst(~/\.[^\.]+$/, '')
        puppetFullyQualifiedClassNames << it.path.replace('./puppetimpl/src/main/groovy/', '').replace('/', '.').replace('.groovy', '')
    }
}

if (puppetClassNames.size() > 0) {
    puppetClassNames.eachWithIndex { it, i ->
        println "${i}) ${it}"
    }
    def selection = inputNumber("Select a puppet", 0, puppetClassNames.size() - 1)
    def selectedPuppet = (InstallablePuppet) this.class.classLoader.loadClass(puppetFullyQualifiedClassNames[selection])?.newInstance()
    processPuppet(selectedPuppet, 0)
} else {
    System.err.println "No puppets found to test, double-check your setup."
}

def int inputNumber(String text, int low, int high) {
    def selection = -1

    while (selection < low || selection >= high) {
        print "\n${text} [${low}-${high}]: "
        try {
            selection = System.in.newReader().readLine() as int
            if (selection >= low && selection <= high) {
                break
            }
        } catch (ignore) {
        }
        selection = -1
        System.err.println "Invalid input, try again."
    }
    return selection
}

def processPuppet(Puppet selectedPuppet, int level) {
    println "-".multiply(80)
    println "${selectedPuppet.name} (${selectedPuppet instanceof SourcesPuppet ? "SourcesPuppet" : "ParentPuppet"})"
    if (selectedPuppet instanceof SourcesPuppet) {
        def children = selectedPuppet.getSources()
        def i = 0
        while (children != null && children.hasNext()) {
            def next = children.next()
            println "\t".multiply(level + 1) + i++ + ") ${next.url}"
        }
    } else if (selectedPuppet instanceof ParentPuppet) {
        def children = selectedPuppet.getChildren()
        def i = 0
        def childPuppets = []
        while (children != null && children.hasNext()) {
            def next = children.next()
            childPuppets << next
            println "\t".multiply(level + 1) + i++ + ") ${next.name} (${next instanceof SourcesPuppet ? "SourcesPuppet" : "ParentPuppet"})"
        }
        def selection = inputNumber("Select a puppet", 0, childPuppets.size() - 1)
        processPuppet(childPuppets[selection] as Puppet, level + 1)
    }
    println "-".multiply(80)
}