import json

from rest_framework.decorators import api_view
from django.http import HttpResponse

from todo.data_store import item_collaborator_store as store
from todo.serializers import UUIDEncoder, UUIDDecoder, ItemCollaboratorSerializer, ItemSerializer


@api_view(['POST'])
def create_item_collaborator(request):
    serializer = ItemCollaboratorSerializer()
    obj = serializer.deserialize(request.body)
    item_collaborator = store.create_item_collaborator(obj)
    if item_collaborator is None:
        return HttpResponse(status=400)
    result = serializer.serialize(item_collaborator)
    return HttpResponse(json.dumps(result, cls=UUIDEncoder))


@api_view(['DELETE'])
def delete_item_collaborator(request, item_id, collaborator_id):
    if store.delete_collaborator(item_id, collaborator_id):
        return HttpResponse()
    else:
        return HttpResponse(status=404)


@api_view(['GET'])
def get_item_collaborators_for_item(request, item_id):
    item_collaborators = store.get_item_collaborators_for_item(item_id)
    serializer = ItemSerializer()
    result = [serializer.serialize(entry) for entry in item_collaborators]
    return HttpResponse(json.dumps(result, cls=UUIDEncoder))
