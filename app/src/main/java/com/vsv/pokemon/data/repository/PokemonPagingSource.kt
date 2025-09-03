package com.vsv.pokemon.data.repository

//class PokemonPagingSource(
//    private val pokemonApi: PokemonApi
//) : PagingSource<Int, PokemonModel>() {
//
//    override fun getRefreshKey(state: PagingState<Int, PokemonModel>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonModel> {
//        val offset = params.key ?: 0
//        try {
//            val response = pokemonApi.getPokemonList(LIMIT, offset)
//            return LoadResult.Page(
//                data = response.results.map { it.toModel() },
//                prevKey = if (offset == 0) null else offset - LIMIT,
//                nextKey = if (response.next == null) null else offset + LIMIT
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }
//
//    companion object {
//        const val LIMIT = 20
//    }
//}