package engine.opengl

open class EnigScope {

	private val resources = ArrayList<Resource>()
	private var subScope : EnigScope? = null

	open fun isLeaf() : Boolean = subScope == null

	open fun addResource(resource : Resource) {
		if (isLeaf()) resources.add(resource) else subScope!!.addResource(resource)
	}

	open fun removeResource(resource : Resource) {
		if (!resources.remove(resource)) {
			subScope?.removeResource(resource)
		}
	}

	open fun exitScope() {
		subScope?.exitScope()
		when {
			subScope == null -> for (r in resources) r.destroy()
			subScope?.isLeaf() == true -> subScope = null
		}
	}

	open fun enterScope() {
		if (subScope == null) subScope = EnigScope() else subScope!!.enterScope()
	}
}