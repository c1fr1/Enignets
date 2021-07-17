package engine.opengl

open class EnigScope {

	private val resources = ArrayList<Resource>()
	private var subScope : EnigScope? = null

	open fun isLeaf() : Boolean = subScope == null

	fun addResource(resource : Resource) {
		if (isLeaf()) resources.add(resource) else subScope!!.addResource(resource)
	}

	fun removeResource(resource : Resource) {
		if (!resources.remove(resource)) {
			subScope?.removeResource(resource)
		}
	}

	fun exitScope() {
		subScope?.exitScope()
		when {
			subScope == null -> for (r in resources) r.destroy()
			subScope?.isLeaf() == true -> subScope = null
		}
	}

	fun enterScope() {
		if (subScope == null) subScope = EnigScope() else subScope!!.enterScope()
	}
}